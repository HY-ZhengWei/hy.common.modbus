package org.hy.common.modbus.modbus4j;


import org.hy.common.modbus.enums.ModbusData;

import com.serotonin.modbus4j.code.DataType;





/**
 * 将统计数据类型转换为Modbus4J类库的数据类型
 * 
 * @see com.serotonin.modbus4j.code.DataType
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-01-20
 * @version     v1.0
 */
public class Modbus4JData
{
    
    private static int [] $To = null;
    
    
    
    /**
     * 初始化
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-20
     * @version     v1.0
     *
     */
    public static void init()
    {
        $To = new int[34];
        
        $To[ModbusData.D1Bit.getValue()                           ] = DataType.BINARY;
                                                       
        $To[ModbusData.D2B_Int_Unsigned.getValue()                ] = DataType.TWO_BYTE_INT_UNSIGNED;
        $To[ModbusData.D2B_Int_Signed.getValue()                  ] = DataType.TWO_BYTE_INT_SIGNED;
        $To[ModbusData.D2B_Int_Unsigned_Swapped.getValue()        ] = DataType.TWO_BYTE_INT_UNSIGNED_SWAPPED;
        $To[ModbusData.D2B_Int_Signed_Swapped.getValue()          ] = DataType.TWO_BYTE_INT_SIGNED_SWAPPED;
                                                       
        $To[ModbusData.D4B_Int_Unsigned.getValue()                ] = DataType.FOUR_BYTE_INT_UNSIGNED;
        $To[ModbusData.D4B_Int_Signed.getValue()                  ] = DataType.FOUR_BYTE_INT_SIGNED;
        $To[ModbusData.D4B_Int_Unsigned_Swapped.getValue()        ] = DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED;
        $To[ModbusData.D4B_Int_Signed_Swapped.getValue()          ] = DataType.FOUR_BYTE_INT_SIGNED_SWAPPED;
        $To[ModbusData.D4B_Int_Unsigned_Swapped_Swapped.getValue()] = DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED_SWAPPED;
        $To[ModbusData.D4B_Int_Signed_Swapped_Swapped.getValue()  ] = DataType.FOUR_BYTE_INT_SIGNED_SWAPPED_SWAPPED;
                                                       
        $To[ModbusData.D4B_Float.getValue()                       ] = DataType.FOUR_BYTE_FLOAT;
        $To[ModbusData.D4B_Float_Swapped.getValue()               ] = DataType.FOUR_BYTE_FLOAT_SWAPPED;
        $To[ModbusData.D4B_Float_Swapped_INVERTED.getValue()      ] = DataType.FOUR_BYTE_FLOAT_SWAPPED_INVERTED;
                                                       
        $To[ModbusData.D8B_Int_Unsigned.getValue()                ] = DataType.EIGHT_BYTE_INT_UNSIGNED;
        $To[ModbusData.D8B_Int_Signed.getValue()                  ] = DataType.EIGHT_BYTE_INT_SIGNED;
        $To[ModbusData.D8B_Int_Unsigned_Swapped.getValue()        ] = DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED;
        $To[ModbusData.D8B_Int_Signed_Swapped.getValue()          ] = DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED;
        $To[ModbusData.D8B_Float.getValue()                       ] = DataType.EIGHT_BYTE_FLOAT;
        $To[ModbusData.D8B_Float_Swapped.getValue()               ] = DataType.EIGHT_BYTE_FLOAT_SWAPPED;
                                                       
        $To[ModbusData.D2B_BCD.getValue()                         ] = DataType.TWO_BYTE_BCD;
        $To[ModbusData.D4B_BCD.getValue()                         ] = DataType.FOUR_BYTE_BCD;
        $To[ModbusData.D4B_BCD_Swapped.getValue()                 ] = DataType.FOUR_BYTE_BCD_SWAPPED;
                                                       
        $To[ModbusData.DChar.getValue()                           ] = DataType.CHAR;
        $To[ModbusData.DVarchar.getValue()                        ] = DataType.VARCHAR;
                                                       
        $To[ModbusData.D4B_MOD_10K.getValue()                     ] = DataType.FOUR_BYTE_MOD_10K;
        $To[ModbusData.D6B_MOD_10K.getValue()                     ] = DataType.SIX_BYTE_MOD_10K;
        $To[ModbusData.D8B_MOD_10K.getValue()                     ] = DataType.EIGHT_BYTE_MOD_10K;
        $To[ModbusData.D4B_MOD_10K_Swapped.getValue()             ] = DataType.FOUR_BYTE_MOD_10K_SWAPPED;
        $To[ModbusData.D6B_MOD_10K_Swapped.getValue()             ] = DataType.SIX_BYTE_MOD_10K_SWAPPED;
        $To[ModbusData.D8B_MOD_10K_Swapped.getValue()             ] = DataType.EIGHT_BYTE_MOD_10K_SWAPPED;
                                                       
        $To[ModbusData.D1B_Int_Unsigned_Lower.getValue()          ] = DataType.ONE_BYTE_INT_UNSIGNED_LOWER;
        $To[ModbusData.D1B_Int_Unsigned_Upper.getValue()          ] = DataType.ONE_BYTE_INT_UNSIGNED_UPPER;
    }
    
    
    
    /**
     * 转换数据类型为Modbus4J类库的数据类型
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-20
     * @version     v1.0
     *
     * @param i_ModbusData
     * @return
     */
    public static int toDataType(int i_ModbusData)
    {
        return $To[i_ModbusData];
    }
    
    
    
    private Modbus4JData()
    {
        // 禁止 new
    }
    
}
