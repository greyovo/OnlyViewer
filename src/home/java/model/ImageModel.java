package model;

import lombok.Data;
import org.junit.Test;

//lombok自动完成getter/setter/toString
@Data
public class ImageModel {
    //单元测试
    @Test
    public void Test1(){
        System.out.println("Test!");
    }



}
