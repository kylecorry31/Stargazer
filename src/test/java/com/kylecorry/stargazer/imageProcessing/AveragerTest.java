package com.kylecorry.stargazer.imageProcessing;

import com.kylecorry.stargazer.OpenCVManager;
import org.junit.After;
import org.junit.Test;

import org.junit.Assert.*;
import org.junit.Before;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class AveragerTest {
        private Averager averager;
        private Mat image1;
        private Mat image2;

        @Before
        public void setup() {
            OpenCVManager.load();
            image1 = Mat.zeros(2, 2, 16);
            image2 = Mat.zeros(2, 2, 16);
            image2.setTo(Scalar.all(2.0));
            averager = new Averager(new Size(2.0, 2.0));
        }

        @After
        public void takedown() {
            image1.release();
            image2.release();
            averager.release();
        }

        @Test
        public void accumulate() {
            averager.accumulate(image1);
            averager.accumulate(image2);
            assertEquals(2, averager.getCount());
            assertEquals(2.0, averager.getAccumulator().get(0, 0)[0], 0.0);
        }

        @Test
        public void getAverage() {
            averager.accumulate(image1);
            averager.accumulate(image2);
            Mat average = averager.getAverage();
            assertEquals(1.0, average.get(0, 0)[0], 0.0);
            average.release();
            averager.accumulate(image2);
            averager.accumulate(image2);
            average = averager.getAverage(); // Converts to integer
            assertEquals(2, average.get(0, 0)[0], 0.001);
            average.release();
        }

        @Test
        public void reset() {
            averager.accumulate(image2);
            averager.reset();
            assertEquals(0.0, averager.getAccumulator().get(0, 0)[0], 0.0);
            assertEquals(0, averager.getCount());
            Mat average = averager.getAverage();
            assertEquals(0.0, average.get(0, 0)[0], 0.0);
            average.release();
        }

        @Test
        public void release() {
            assertNotEquals(0, averager.getAccumulator().dataAddr());
            averager.release();
            assertEquals(0, averager.getAccumulator().dataAddr());
        }

}
