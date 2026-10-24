/*-
 * #%L
 * A library that allows the connection of ImageJ and OpenCV.
 * %%
 * Copyright (C) 2017 - 2026 University of La Rioja
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package ijopencv.examples;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import ij.IJ;
import ij.ImagePlus;
import ij.gui.OvalRoi;
import ij.plugin.filter.PlugInFilter;
import ij.plugin.frame.RoiManager;
import ij.process.ImageProcessor;
import ijopencv.ij.ImagePlusMatConverter;
import ijopencv.opencv.MatListOvalRoiConverter;
import java.util.ArrayList;
import net.imagej.ImageJ;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgproc;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 *
 * @author jonathan
 */
@Plugin(type = Command.class, headless = true, menuPath = "Plugins>IJ-OpenCV-plugins>Detect circles")
public class DetectCirclesJ_ implements Command {

    @Parameter
    private ImagePlus imp;

    @Override
    public void run() {

        // Converters
        ImagePlusMatConverter ic = new ImagePlusMatConverter();
        opencv_core.Mat m = ic.convert(imp, Mat.class);
        MatListOvalRoiConverter cc = new MatListOvalRoiConverter();

        Mat gray = new Mat();
        opencv_imgproc.cvtColor(m, gray, opencv_imgproc.COLOR_BGR2GRAY);

        Mat circles = new Mat();
        opencv_imgproc.HoughCircles(gray, circles, opencv_imgproc.CV_HOUGH_GRADIENT, 1.2, 100);

        ArrayList<OvalRoi> or = new ArrayList<OvalRoi>();
        or = cc.convert(circles, or.getClass());

        RoiManager rm = new RoiManager();

        for (int i = 0; i < or.size(); i++) {
            rm.add(imp, or.get(i), i);
        }

    }

    public static void main(final String... args) throws Exception {
        // Launch ImageJ as usual.
        final ImageJ ij = new ImageJ();
        ij.launch(args);
    }

}
