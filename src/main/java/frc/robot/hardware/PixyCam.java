package frc.robot.hardware;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogOutput;

import frc.robot.hardware.pixy2api.*;
import frc.robot.hardware.pixy2api.Pixy2CCC.Block;
import frc.robot.software.*;


public class PixyCam {

    //PIXY 2 Resolution: 1296x976
    //XY Res: 315x205

    private static AnalogInput pixy;
    public static AnalogOutput led;
    public static boolean isLedOn = false;
    private static Pixy2CCC ccc;
    private static ArrayList<Block> blockBuffer;

    public static void initialize() {

        
        pixy = new AnalogInput(Statics.PIXY_CAM);
        led  = new AnalogOutput(0);
        switchLED(true);

        //ccc = new Pixy2().getCCC();

    }



    public static double getTargetLocation() {
        //return (double)(getHighPort().getX()) / 315 * 2 - 1;
        return (pixy.getAverageVoltage()/3.3 * 2) - 1;
    }

    /*
    private static Block getHighPort() {
        
        Integer goodBlocks = 0;
        int counter = 0;
        Block biggest = new Block(0, 0, 0, 0, 0, 0, 0, 0);

        ccc.fetchData(false, Pixy2CCC.CCC_SIG1,10,blockBuffer,goodBlocks);

        if(goodBlocks == 0) return biggest;

        for(Block block : blockBuffer) {
            counter ++;

            if(counter == 1) {
                biggest = block;
            }
            else {
                if(block.getWidth() > biggest.getWidth()) {
                    biggest = block;
                }
            }
        }

        return biggest;
    }
    */

    public static void switchLED(boolean on) {
        isLedOn = on;
        if(on) {
            Utils.report("LED On");
            led.setVoltage(5);
        }
        else {
            Utils.report("LED Off");
            led.setVoltage(0);
        }
    }
} 