package org.hy.common.modbus.enums;





/**
 * 奇偶校验的枚举
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-01-23
 * @version     v1.0
 */
public enum Parity
{
    
    None (0 ,"无校验"),
    
    Odd  (1 ,"奇校验"),
    
    Even (2 ,"偶校验"),
    
    Mark (3 ,"标志校验"),
    
    Space(4 ,"空白校验"),
    
    ;
    
    
    
    /** 值 */
    private Integer value;
    
    /** 描述 */
    private String  comment;
    
    
    
    /**
     * 数值转为常量
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-07-29
     * @version     v1.0
     *
     * @param i_Value
     * @return
     */
    public static Parity get(Integer i_Value)
    {
        if ( i_Value == null )
        {
            return null;
        }
        
        for (Parity v_Enum : Parity.values())
        {
            if ( v_Enum.value.equals(i_Value) )
            {
                return v_Enum;
            }
        }
        
        return null;
    }
    
    
    
    Parity(Integer i_Value ,String i_Comment)
    {
        this.value   = i_Value;
        this.comment = i_Comment;
    }

    
    
    public Integer getValue()
    {
        return this.value;
    }
    
    
    
    public String getComment()
    {
        return this.comment;
    }
    
    

    public String toString()
    {
        return this.value + "";
    }
    
}
