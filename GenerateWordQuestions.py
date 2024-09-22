import packages as p

# p.nltk.download('stopwords')

# Use NLTK's stopwords list
STOPWORDS = set(p.stopwords.words('english'))

# Initialize the translator using deep_translator
translator = p.GoogleTranslator(source='en', target='ko')

# Cache for storing translations
translation_cache = {}

# Function to get distractors for a given word, ensuring uniqueness and relevance
def get_distractors(word, pos, num_distractors=3):
    distractors = set()
    synsets = p.wn.synsets(word, pos=pos)
    if synsets:
        for syn in synsets:
            for lemma in syn.lemmas():
                name = lemma.name().replace('_', ' ')
                if name.lower() != word.lower() and name not in distractors:
                    distractors.add(name)
                if len(distractors) >= num_distractors:
                    break
            if len(distractors) >= num_distractors:
                break
    return list(distractors)

# Function to select key words from a sentence, excluding common words
def select_keywords(sentence):
    tokens = p.nltk.word_tokenize(sentence)
    pos_tags = p.nltk.pos_tag(tokens)
    keywords = [word for word, pos in pos_tags if pos.startswith(('NN', 'VB', 'JJ')) and word.lower() not in STOPWORDS]
    return keywords

# Function to translate with caching and error handling
def translate_word(word):
    if word in translation_cache:
        return translation_cache[word]

    try:
        translation = translator.translate(word)
        translation_cache[word] = translation
        return translation
    except Exception as e:
        print(f"Translation error for '{word}': {e}")
        return "번역 없음"

# Function to generate MCQs with Korean meanings, ensuring unique options and limiting to 5 questions per paragraph
def generate_mcq_with_korean_meaning(sentence, num_options=4, max_questions=5):
    mcqs = []
    keywords = select_keywords(sentence)

    # Ensure we attempt to generate up to max_questions even if some fail
    question_count = 0

    for keyword in keywords:
        if question_count >= max_questions:
            break

        pos = p.wn.synsets(keyword)[0].pos() if p.wn.synsets(keyword) else 'n'
        distractors = get_distractors(keyword, pos, num_distractors=num_options - 1)

        # Skip this keyword if there aren't enough distractors
        if len(distractors) < num_options - 1:
            print(f"Not enough distractors for '{keyword}'. Skipping...")
            continue

        # Translate keyword and distractors to Korean using caching and error handling
        correct_translation = translate_word(keyword)
        options = [correct_translation] + [translate_word(d) for d in distractors]

        # Ensure options are unique and fill up to num_options if needed
        options = list(set(options))

        # If still not enough options, generate random words as additional distractors from existing keywords
        while len(options) < num_options:
            potential_distractor = translate_word(p.random.choice(keywords))
            if potential_distractor not in options:
                options.append(potential_distractor)

            # Break loop if unable to find unique distractors after several attempts (to avoid infinite loop)
            if len(options) >= num_options or len(potential_distractor) == 0:
                break

        p.random.shuffle(options)

        mcqs.append({
            'question': f"단어 '{keyword}'의 한국어 뜻을 고르세요.",
            'options': options,
            'answer': correct_translation
        })

        question_count += 1

    return mcqs

# Example usage with delimiter between paragraphs
if __name__ == "__main__":
    file_path = 'data/output_data.xlsx'
    key_paragraphs_df = p.pd.read_excel(file_path, usecols=['Key Paragraphs'])

    for _, row in key_paragraphs_df.iterrows():
        key_paragraph = row['Key Paragraphs']  # Extract the actual text from the row
        print(key_paragraph)
        mcqs = generate_mcq_with_korean_meaning(key_paragraph)

        for mcq in mcqs:
            print(f"문제: {mcq['question']}")
            for idx, option in enumerate(mcq['options'], 1):
                print(f"{idx}. {option}")
            print(f"정답: {mcq['answer']}\n")