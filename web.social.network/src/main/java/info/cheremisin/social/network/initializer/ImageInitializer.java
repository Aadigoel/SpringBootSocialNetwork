package info.cheremisin.social.network.initializer;

import info.cheremisin.social.network.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;


@Component
@RequiredArgsConstructor
public class ImageInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final ImageService imageService;

    @Value("classpath:profileImages/*")
    private Resource[] resources;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            Path pathImages = imageService.getProfileImagesPath();
            if(pathImages.toFile().exists()) {
                Files.walk(pathImages)
                        .map(Path::toFile)
                        .forEach(File::delete);
                Files.delete(pathImages);
            }
            Files.createDirectory(pathImages);

            for (Resource resource : resources) {
                InputStream inputStream = resource.getInputStream();
                Files.copy(inputStream, pathImages.resolve(resource.getFilename()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



// The class implements the ApplicationListener interface with the generic type ContextRefreshedEvent, which means that the onApplicationEvent method will be called when the ContextRefreshedEvent is published.

// The ImageInitializer class uses ImageService, which is a service responsible for managing profile images. The service is autowired using the @Autowired annotation and passed to the class constructor with @RequiredArgsConstructor Lombok annotation.

// The class has a private field resources of type Resource[]. The @Value annotation is used to inject the classpath:profileImages/* resource pattern into this field. This means that all files matching this pattern in the profileImages directory in the classpath will be loaded as resources.

// The onApplicationEvent method is called when the application context is refreshed. The method first gets the path to the profile images directory using the ImageService and deletes all existing files in it. It then creates a new directory for the profile images.

// Next, the method iterates over the resources array and copies each resource to the profile images directory using the Files.copy method.

// In summary, this class is responsible for initializing the profile images for a social network application by loading them from the classpath and copying them to the appropriate directory.