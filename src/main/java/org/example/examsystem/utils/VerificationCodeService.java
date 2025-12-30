package org.example.examsystem.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单的验证码存储与校验（内存版，5分钟有效）
 */
@Component
public class VerificationCodeService {

    private static final long EXPIRE_MILLIS = 5 * 60 * 1000L;
    private final Map<String, CodeEntry> store = new ConcurrentHashMap<>();
    private final Random random = new Random();

    /**
     * 生成6位数字验证码并保存
     */
    public String generate(String email) {
        String code = String.format("%06d", random.nextInt(1_000_000));
        store.put(email, new CodeEntry(code, System.currentTimeMillis() + EXPIRE_MILLIS));
        return code;
    }

    /**
     * 校验验证码是否匹配且未过期
     */
    public boolean validate(String email, String code) {
        CodeEntry entry = store.get(email);
        if (entry == null) {
            return false;
        }
        if (entry.expireAt < Instant.now().toEpochMilli()) {
            store.remove(email);
            return false;
        }
        boolean ok = entry.code.equals(code);
        if (ok) {
            store.remove(email); // 用后即焚
        }
        return ok;
    }

    private static class CodeEntry {
        String code;
        long expireAt;

        CodeEntry(String code, long expireAt) {
            this.code = code;
            this.expireAt = expireAt;
        }
    }
}

