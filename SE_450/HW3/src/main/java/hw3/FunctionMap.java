package hw3;

import java.util.function.Function;

public class FunctionMap implements Function {

    /**
     * Applies this function to the given argument.
     *
     * @param o the function argument
     * @return the function result
     */
    @Override
    public Object apply(Object o) {
        return o.hashCode();
    }

}
