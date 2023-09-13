package com.freedom.model;

import java.io.Serializable;
import lombok.Data;

/**
 * user
 * @author 
 */
@Data
public class User implements Serializable {
    private Long id;

    /**
     * 余额
     */
    private Long money;

    private static final long serialVersionUID = 1L;
}