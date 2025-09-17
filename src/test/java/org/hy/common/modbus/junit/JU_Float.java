package org.hy.common.modbus.junit;


public class JU_Float
{
    
    public static void main(String[] args) {
        int reg1 = 7;
        int reg2 = 41248;
        
        System.out.println("测试其他数据格式:");
        
        // 1. 32位有符号整数
        int asInt = (reg1 << 16) | reg2;
        System.out.println("作为32位整数: " + asInt);
        
        // 2. 32位无符号整数
        long asUnsignedInt = ((long) reg1 << 16) | reg2;
        System.out.println("作为无符号32位整数: " + asUnsignedInt);
    }
    
}
