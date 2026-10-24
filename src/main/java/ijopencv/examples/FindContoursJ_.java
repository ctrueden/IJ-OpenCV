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
import ij.gui.PolygonRoi;
import ij.plugin.filter.PlugInFilter;
import ij.plugin.frame.RoiManager;
import ij.process.ImageProcessor;
import ijopencv.ij.ImagePlusMatConverter;
import ijopencv.opencv.MatVectorListPolygonRoiConverter;

import java.util.ArrayList;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_imgproc;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 *
 * @author jonathan
 */
@Plugin(type = Command.class, headless = true, menuPath = "Plugins>IJ-OpenCV-plugins>Find contours")
public class FindContoursJ_ implements Command {

   @Parameter
    private ImagePlus imp;



    @Override
    public void run() {
        
        // Converters
        ImagePlusMatConverter ic = new ImagePlusMatConverter();

        opencv_core.Mat m = ic.convert(imp,opencv_core.Mat.class);
        MatVectorListPolygonRoiConverter pc = new MatVectorListPolygonRoiConverter();
        
        
        opencv_core.Mat gray = new opencv_core.Mat();
        opencv_imgproc.cvtColor(m,gray,opencv_imgproc.COLOR_BGR2GRAY);
        
        MatVector contours = new opencv_core.MatVector();
        
        opencv_imgproc.findContours(gray,contours,opencv_imgproc.RETR_LIST,opencv_imgproc.CHAIN_APPROX_SIMPLE);
        
        ArrayList<PolygonRoi> contoursROI = new ArrayList<PolygonRoi>();
        contoursROI= pc.convert(contours,contoursROI.getClass());
        
         // Add rectangles to ROI Manager
        RoiManager rm = new RoiManager();
        rm.setVisible(true);
        for (PolygonRoi contoursROI1 : contoursROI) {
            rm.add(imp, contoursROI1, 0);
        }
        
        
        
    
    }
    
}
