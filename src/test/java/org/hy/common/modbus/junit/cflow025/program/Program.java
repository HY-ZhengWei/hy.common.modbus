package org.hy.common.modbus.junit.cflow025.program;

import java.util.ArrayList;
import java.util.List;

import org.hy.common.Date;
import org.hy.common.Help;





/**
 * 模拟被编排的程序 
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-07-14
 * @version     v1.0
 */
public class Program
{
    
    /** 历史数据 */
    private static List<LEDData> $History  = new ArrayList<LEDData>();
    
    /** 历史数据的位置 */
    private static int           $Index    = -1;
    
    
    
    /**
     * 生成新数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-07-14
     * @version     v1.0
     *
     */
    private void newData()
    {
        int  v_TimeGS = 5;
        Date v_Time   = new Date();
        
        v_Time = v_Time.getTimeGroupSecond(v_TimeGS);
        
        LEDData v_Data = new LEDData();
        v_Data.setTime(v_Time);
        v_Data.setTemperature(Help.random(0 ,2000));
        
        if ( $Index >= 0 )
        {
            if ( $History.get($Index).getTime().equals(v_Time) )
            {
                // 相同时间内的数据仅保留一个
                return;
            }
        }
        
        $History.add(v_Data);
        $Index++;
    }
    
    
    
    /**
     * 返回一组数据，一组4行数据，最新的一行数据在返回结果的首个元素位置上
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-07-14
     * @version     v1.0
     *
     * @return
     */
    public List<LEDData> getDatas()
    {
        this.newData();

        List<LEDData> v_RetDatas = new ArrayList<LEDData>();
        for (int x=0; x<=3; x++)
        {
            if ( $Index - x >= 0 )
            {
                v_RetDatas.add($History.get($Index - x));
            }
            else
            {
                v_RetDatas.add(new LEDData());
            }
        }
        
        return v_RetDatas;
    }
    
}
