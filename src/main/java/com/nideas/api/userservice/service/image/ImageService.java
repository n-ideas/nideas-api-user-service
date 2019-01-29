package com.nideas.api.userservice.service.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Created by Nanugonda on 9/12/2018. */
@Service
public class ImageService {

  public boolean isValidImage(MultipartFile multipartFile) {
    boolean validImage = false;
    boolean fallbackOnApacheCommonsImaging;
    String formatName;
    try (ImageInputStream imageInputStream =
        ImageIO.createImageInputStream(multipartFile.getInputStream())) {
      Iterator<ImageReader> imageReaderIterator = ImageIO.getImageReaders(imageInputStream);
      // If there not ImageReader instance found so it's means that the current format is not
      // supported by the Java built-in API
      if (!imageReaderIterator.hasNext()) {
        ImageInfo imageInfo = Imaging.getImageInfo(multipartFile.getBytes());
        if (imageInfo != null
            && imageInfo.getFormat() != null
            && imageInfo.getFormat().getName() != null) {
          formatName = imageInfo.getFormat().getName();
          fallbackOnApacheCommonsImaging = true;
        } else {
          throw new IOException(
              "Format of the original image is not supported for read operation !");
        }
      } else {
        ImageReader reader = imageReaderIterator.next();
        formatName = reader.getFormatName();
        fallbackOnApacheCommonsImaging = false;
      }

      // Load the image
      BufferedImage originalImage;
      if (!fallbackOnApacheCommonsImaging) {
        originalImage = ImageIO.read(multipartFile.getInputStream());
      } else {
        originalImage = Imaging.getBufferedImage(multipartFile.getInputStream());
      }

      // Check that image has been successfully loaded
      if (originalImage == null) {
        throw new IOException("Cannot load the original image !");
      }
      validImage = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return validImage;
  }
}
