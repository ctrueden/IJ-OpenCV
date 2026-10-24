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


import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ijopencv.ij.ImagePlusMatConverter;

import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_core.multiply;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import org.bytedeco.javacpp.opencv_photo;
import static org.bytedeco.javacpp.opencv_photo.createMergeMertens;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jonathan
 */
@Plugin(type = Command.class, headless = true, menuPath = "Plugins>IJ-OpenCV-plugins>High dynamic range imaging")
public class High_Dynamic_Range_ImagingJ_ implements Command {

 
    @Parameter
    private ImagePlus imp;

    @Override
    public void run() {
        // Converters

        ImagePlusMatConverter isc = new ImagePlusMatConverter();

        opencv_core.MatVector mvec = isc.convert(imp,opencv_core.MatVector.class);
        opencv_core.Mat res = new opencv_core.Mat();
        opencv_photo.MergeMertens merge_mertens = createMergeMertens();
        merge_mertens.process(mvec, res);

        opencv_core.MatExpr me = multiply(255, res);

        imshow("res", me.a());
        waitKey(0);
    }

}
