/**
 * The operator combines all values in the given range into one value
 * using combiner and initial value (seed)
 */
public static final BiFunction<Integer, IntBinaryOperator, IntBinaryOperator> reduceIntOperator=
        (seed,combinator)->(left,right)->{
        var result=seed;
        for(var i=left;i<=right;i++){
        result=combinator.applyAsInt(result,i);
        }
        return result;
        };

/**
 * The operator calculates the sum in the given range (inclusively)
 */
public static final IntBinaryOperator sumOperator= // write your code here
        reduceIntOperator.apply(0,(x,y)->x+y);

/**
 * The operator calculates the product in the given range (inclusively)
 */
public static final IntBinaryOperator productOperator= // write your code here
        reduceIntOperator.apply(1,(x,y)->x*y);
