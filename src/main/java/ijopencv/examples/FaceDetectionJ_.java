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
import ij.gui.Roi;
import ij.plugin.frame.RoiManager;
import ijopencv.ij.ImagePlusMatConverter;
import ijopencv.opencv.RectRoiConverter;
import java.nio.file.Paths;
import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import org.bytedeco.javacpp.opencv_objdetect;
import static org.bytedeco.javacpp.opencv_objdetect.CASCADE_SCALE_IMAGE;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 *
 * @author jonathan
 */
@Plugin(type = Command.class, headless = true, menuPath = "Plugins>IJ-OpenCV-plugins>Face detection")
public class FaceDetectionJ_ implements Command {

    @Parameter
    private ImagePlus imp;

    @Override
    public void run() {
        //Converters
        RectRoiConverter rc = new RectRoiConverter();
        opencv_core.Mat img2 = ImagePlusMatConverter.toMat(imp, 8); // also does RGB to Gray automatically

        // Detect the faces and store them as an array of rectangles
        opencv_core.RectVector rv = detectFaces(img2);

        // Add rectangles to ROI Manager
        RoiManager rm = new RoiManager();
        rm.setVisible(true);
        for (int i = 0; i < rv.size(); i++) {
            Roi r = rc.convert(rv.get(i), Roi.class);
            rm.add(imp, r, 0);
        }
        
        //Show all ROI
        rm.runCommand("Show All");

    }

    public opencv_core.RectVector detectFaces(opencv_core.Mat image) {
        
        // Open xml classifier located in Fiji.app/lib (provided by IJ-OpenCV update site)
        opencv_objdetect.CascadeClassifier faceclassifier = new opencv_objdetect.CascadeClassifier(Paths.get(IJ.getDirectory("imagej"),"lib","haarcascade_frontalface_alt.xml").toString());

        opencv_core.RectVector rv = new opencv_core.RectVector();

        faceclassifier.detectMultiScale(image, rv, 1.1, 2, CASCADE_SCALE_IMAGE, new opencv_core.Size(30, 30), new opencv_core.Size(500, 500));
        
        faceclassifier.close(); //prevent possible memory leak
        
        return rv;

    }

}
