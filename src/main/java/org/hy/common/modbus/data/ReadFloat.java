package org.hy.common.modbus.data;

import org.hy.common.xml.annotation.Xjava;





/**
 * 高低位转为浮点数的方法类
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-10-13
 * @version     v1.0
 */
@Xjava
public class ReadFloat
{
    
    /**
     * 高低位两整数转为浮点数。算法：高低位合并 + 缩放还原
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-09-17
     * @version     v1.0
     *
     * @param i_High  高位数值
     * @param i_Low   低位数值
     * @return
     */
    public Float toFloat(int i_High ,int i_Low)
    {
        return (((long) i_High << 16) | i_Low) * 1F;
    }
    
    
    
    /**
     * 直接用两个 16 位寄存器，拼接成一个 32 位 IEEE 754 单精度浮点数（float），不需要自己除以比例因子。算法：IEEE 754 标准浮点解析
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-10-13
     * @version     v1.0
     *
     * @param i_High  高位数值
     * @param i_Low   低位数值
     * @return
     */
    public Float toFloatIEEE754(int i_High ,int i_Low)
    {
        int v_Combined = (i_High << 16) | (i_Low & 0xFFFF);
        return Float.intBitsToFloat(v_Combined);
    }
    
}
