package de.featjar.comparison.test.helper;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class Result<T> {
    @Nullable
    final T result;

    final boolean isDecidable;

    public Result() {
        this.result = null;
        this.isDecidable = false;
    }

    public Result(@Nullable T result) {
        this.result = result;
        this.isDecidable = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Result) {
            try {
                Result<Object> other = (Result<Object>) obj;
                if (other.isDecidable != isDecidable) {
                    return false;
                }
                if (other.result == null && result == null) {
                    return true;
                }
                if (other.result != null) {
                    // TODO compare Sets without taking into account the order
                    return other.result.equals(result);
                }
                // other.result == null && result != null
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }
        return false;
    }

    public static <S> Result<S> get(Supplier<Result<S>> f) {
        try {
            return f.get();
        } catch (Exception e) {
            return new Result<S>();
        }
    }
}
