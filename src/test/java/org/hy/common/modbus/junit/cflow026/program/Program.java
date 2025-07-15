package org.hy.common.modbus.junit.cflow026.program;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.modbus.junit.cflow025.program.LEDData;





/**
 * 模拟被编排的程序 
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-07-15
 * @version     v1.0
 */
public class Program
{
    
    /** 历史数据 */
    private static LEDData $History = null;
    
    private static LEDData $Org2    = null;
    
    
    
    /**
     * 生成新数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-07-15
     * @version     v1.0
     *
     */
    private LEDData newData()
    {
        LEDData v_Data = new LEDData();
        v_Data.setTime(new Date());
        v_Data.setTemperature(Help.random(0 ,2000));
        
        return v_Data;
    }
    
    
    
    /**
     * 返回一行数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-07-15
     * @version     v1.0
     *
     * @return
     */
    public LEDData getData()
    {
        return this.newData();
    }
    
    
    
    /**
     * 是否写数据库
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-07-15
     * @version     v1.0
     *
     */
    public boolean isWriteDB(LEDData i_Temperature ,LEDDatas i_AgentDatas)
    {
        if ( i_Temperature == null )
        {
            return false;
        }
        if ( i_Temperature.getTemperature() == null )
        {
            return false;
        }
        if ( i_Temperature.getTime() == null )
        {
            return false;
        }
        if ( Help.isNull(i_AgentDatas) )
        {
            return false;
        }
        
        if ( $Org2 == null )
        {
            $Org2 = i_Temperature;
        }
        
        if ( i_AgentDatas.getDataItems().size() < 4 )
        {
            return false;
        }
        if ( i_AgentDatas.getDataItems().get(1).getTemperature() == null )
        {
            return false;
        }
        if ( i_AgentDatas.getDataItems().get(1).getTime() == null )
        {
            return false;
        }
        
        boolean v_Ret = false;
        if ( $History == null)
        {
            $History = i_AgentDatas.getDataItems().get(1);
            $History.setTemperatureOrg($Org2.getTemperature());
            $Org2    = i_Temperature;
            v_Ret    = false;
        }
        else
        {
            if ( $History.getTime().equals(i_AgentDatas.getDataItems().get(1).getTime()) )
            {
                v_Ret = false;
            }
            else
            {
                $History = i_AgentDatas.getDataItems().get(1);
                $History.setTemperatureOrg($Org2.getTemperature());
                $Org2    = i_Temperature;
                v_Ret    = true;
            }
        }
        
        System.out.println(i_Temperature.getTimeString() + " " + i_Temperature.getTemperatureString() + " -> " + i_AgentDatas.getDataItems().get(0).getTemperatureString() + " " + (v_Ret ? "写 " + $History.getTemperatureOrg() : " "));
        return v_Ret;
    }
    
}
