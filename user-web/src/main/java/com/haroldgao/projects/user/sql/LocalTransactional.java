package com.haroldgao.projects.user.sql;

import java.lang.annotation.*;
import java.sql.Connection;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalTransactional {
    int PROPAGATION_REQUIRED = 0;

    /**
     * Transaction propagation
     * @return
     */
    int propagation() default PROPAGATION_REQUIRED;

    /**
     * Transaction isolation level
     * @return
     * @see Connection#TRANSACTION_READ_COMMITTED
     */
    int isolation() default Connection.TRANSACTION_READ_COMMITTED;
}
