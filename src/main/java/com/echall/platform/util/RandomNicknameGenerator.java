package com.echall.platform.util;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class RandomNicknameGenerator {
	private static final List<String> prefixNicknames = List.of(new String[] {"pre1", "pre2", "pre3"});
	private static final List<String> suffixNicknames = List.of(new String[] {"suf1", "suf2", "suf3"});
	private static final Random random = new Random();

	public static String setRandomNickname() {
		return prefixNicknames.get((int)(Math.random() % 3)) +
			suffixNicknames.get((int)(Math.random() % 3)) +
			String.format("%04d", random.nextInt(10000));
	}

}
