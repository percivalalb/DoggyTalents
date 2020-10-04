package doggytalents.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import doggytalents.common.util.Cache;

public class CacheTest {

    @Test
    public void test() {
        Cache<String> cache = Cache.make(() -> new String("string"));
        String first = cache.get();
        String second = cache.get();

        cache.markForRefresh();
        String third = cache.get();

        assertSame(first, second);
        assertNotSame(second, third);

        assertTrue(cache.test((v) -> v.length() == 6));
    }
}
