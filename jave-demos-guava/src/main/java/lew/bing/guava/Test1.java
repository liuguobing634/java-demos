package lew.bing.guava;


import java.util.Optional;

import static com.google.common.base.Preconditions.*;

public class Test1 {

    public static void main(String[] args) {
        Optional<String> optional = Optional.ofNullable("hello");
        System.out.println(optional.isPresent());
         testCheckArgument(null);

    }

    public static void testCheckArgument(String s) {
        checkArgument(s != null,"传参空");
        System.out.println(s);
    }

}
