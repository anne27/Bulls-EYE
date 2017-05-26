package com.example.anannyauberoi.testingcamtwo;
import org.opencv.*;
/**
 * Created by AnannyaUberoi on 25-05-2017.
 */

/*
    This class is used to call the native (C/C++) functions in Android's main activity.
 */

public class cvClass {
    public native static String detect(long addrRgba);          //Declare the native function here.
}
