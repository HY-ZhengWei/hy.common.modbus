package org.hy.common.modbus.enums;





/**
 * Modbus统一数据类型
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-01-20
 * @version     v1.0
 */
public enum ModbusData
{
    
    D1Bit                            (1  ,"1位"),
    
    D2B_Int_Unsigned                 (2  ,"短整数，无符号的"),
    D2B_Int_Signed                   (3  ,"短整数，有符号的"),
    D2B_Int_Unsigned_Swapped         (22 ,"短整数，无符号的、顺序调换"),
    D2B_Int_Signed_Swapped           (23 ,"短整数，有符号的、顺序调换"),
    
    D4B_Int_Unsigned                 (4  ,"整数，无符号的"),
    D4B_Int_Signed                   (5  ,"整数，有符号的"),
    D4B_Int_Unsigned_Swapped         (6  ,"整数，无符号的、顺序调换"),
    D4B_Int_Signed_Swapped           (7  ,"整数，有符号的、顺序调换"),
    D4B_Int_Unsigned_Swapped_Swapped (24 ,"整数，无符号的、顺序调换两次"),
    D4B_Int_Signed_Swapped_Swapped   (25 ,"整数，无符号的、顺序调换两次"),
    
    D4B_Float                        (8  ,"短浮点数"),
    D4B_Float_Swapped                (9  ,"短浮点数，顺序调换"),
    D4B_Float_Swapped_INVERTED       (21 ,"短浮点数，顺序调换、反相的"),
    
    D8B_Int_Unsigned                 (10 ,"长整数，无符号的"),
    D8B_Int_Signed                   (11 ,"长整数，有符号的"),
    D8B_Int_Unsigned_Swapped         (12 ,"长整数，无符号的、顺序调换"),
    D8B_Int_Signed_Swapped           (13 ,"长整数，有符号的、顺序调换"),
    D8B_Float                        (14 ,"浮点数"),
    D8B_Float_Swapped                (15 ,"浮点数，顺序调换"),
    
    D2B_BCD                          (16 ,"Decimal短"),
    D4B_BCD                          (17 ,"Decimal"),
    D4B_BCD_Swapped                  (20 ,"Decimal，顺序调换"),
    
    DChar                            (18 ,"Char"),
    DVarchar                         (19 ,"Varchar"),
    
    D4B_MOD_10K                      (26 ,"短功能码"),
    D6B_MOD_10K                      (27 ,"功能码"),
    D8B_MOD_10K                      (28 ,"长功能码"),
    D4B_MOD_10K_Swapped              (29 ,"短功能码，顺序调换"),
    D6B_MOD_10K_Swapped              (30 ,"功能码，顺序调换"),
    D8B_MOD_10K_Swapped              (31 ,"长功能码，顺序调换"),
    
    D1B_Int_Unsigned_Lower           (32 ,"ASCII小写，无符号的"),
    D1B_Int_Unsigned_Upper           (33 ,"ASCII大写，无符号的"),
    
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
    public static ModbusData get(Integer i_Value)
    {
        if ( i_Value == null )
        {
            return null;
        }
        
        for (ModbusData v_Enum : ModbusData.values())
        {
            if ( v_Enum.value.equals(i_Value) )
            {
                return v_Enum;
            }
        }
        
        return null;
    }
    
    
    
    ModbusData(Integer i_Value ,String i_Comment)
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
