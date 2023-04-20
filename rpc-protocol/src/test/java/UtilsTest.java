import org.junit.jupiter.api.Test;

/**
 * @author: Nyquist Data Tech Team
 * @version: 1.0
 * @date: 2023/4/19
 * @description:
 */
public class UtilsTest {

    @Test
    public void test() {
        Long maxInt = (long) Math.pow(2, 32);
        System.out.println(maxInt);
        System.out.println(Integer.MAX_VALUE);
        int gBytes= 1024 *1024 * 1024;
        System.out.println(gBytes);
        System.out.println(maxInt / 1024/1024/1024);

    }
}
