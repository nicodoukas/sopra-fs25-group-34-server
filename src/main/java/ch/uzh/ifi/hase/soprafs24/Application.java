package ch.uzh.ifi.hase.soprafs24;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ch.uzh.ifi.hase.soprafs24.entity.ProfilePicture;
import ch.uzh.ifi.hase.soprafs24.repository.PictureRepository;

@RestController
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public String helloWorld() {
    return "The application is running.";
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
      }
    };
  }

  //store predefined profilepictures
    @Bean
    public CommandLineRunner seedProfilePictures(PictureRepository pictureRepository) {
        return args -> {
            //Only if DB is empty
            if (pictureRepository.count() == 0) {
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/premium-vector/profile-icon_838328-1033.jpg"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-sloth-yoga-cartoon-icon-illustration_138676-2250.jpg?t=st=1746434375~exp=1746437975~hmac=0f3d9b5ab5c5488457dbdcdf49ef0951b46f5753eb55fa381153e380f6c4c139&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-happy-penguin-cartoon-icon-illustration-animal-nature-icon-concept-isolated-flat-cartoon-style_138676-2095.jpg?t=st=1746434349~exp=1746437949~hmac=2dc45e3beb1d11bcf2ce1698a5d012cffce8a485cae3eeb580b05a2bf7b86951&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-blue-tyrannosaurus-rex_138676-2073.jpg?t=st=1746434394~exp=1746437994~hmac=620da4fb943e5d339cd9ec9bae6ef3723d16d4dcc12ec4b3637f7604634eba69&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-panda-with-bamboo_138676-3053.jpg?t=st=1746434446~exp=1746438046~hmac=e23408eabdc99a4eeb95ec967b9f3c83f4fcc342df8dd87f885546319b870454&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-chameleon-tree-cartoon-illustration_138676-3262.jpg?t=st=1746434767~exp=1746438367~hmac=967464672a57134db6bd2a8e6056000875f6abb67466bea1e04fc7be9bea5602&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-hamster-holding-cheek-cartoon-illustration_138676-2773.jpg?t=st=1746434788~exp=1746438388~hmac=214c610213abd87c011478b7875d7b5d768298f9b53f6ae11f81647ecb2ce364&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-corgi-dog-eating-bone-cartoon_138676-2534.jpg?t=st=1746434806~exp=1746438406~hmac=6704acb04663f968a4c7f32b826ef6b56d47eaec57b9e9195578851d2ec236da&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-astronaut-holding-star-illustration_138676-3293.jpg?t=st=1746434843~exp=1746438443~hmac=449f5e4f60a3edb2d83963baa5aaac9d9bf1715d636cc22b438b3cb5e941884a&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cool-monkey-holding-money-bag-cartoon-vector-icon-illustration-animal-finance-icon-concept-isolated-premium-vector-flat-cartoon-style_138676-3366.jpg?t=st=1746434856~exp=1746438456~hmac=0c3ca597b344b2dfa3885df99b04c712c7c93110bd2ce87a6786b7f78767056b&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-shark-fish-cartoon-vector-icon-illustration-animal-nature-icon-concept-isolated-premium-vector-flat-cartoon-style_138676-3791.jpg?t=st=1746434880~exp=1746438480~hmac=54fedc5c08add7f9bd5a06cf096e6c1006b490d7991c1ac9133e510761a5aa26&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-koala-with-cub-cartoon-icon-illustration_138676-2839.jpg?t=st=1746434882~exp=1746438482~hmac=e34bf297cc09f17a6044f0ba1b62cced485d5a5717ac5fb1239c75780b65e3df&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-raccoon-flying-with-magic-broom-cartoon-vector-icon-illustration-animal-holiday-isolated-flat_138676-12630.jpg?t=st=1746434463~exp=1746438063~hmac=62796c5160fd1d1770d38da426368bffb5d454d8dae16703e9d23a0a044be858&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-brown-sitting-with-hat-cartoon-vector-icon-illustration-animal-nature-icon-concept-isolated_138676-5347.jpg?t=st=1746434957~exp=1746438557~hmac=a0742a8caefd71e9a695f40f4de6801d96af02be25bc84b07728eb011cd38e86&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-hedgehog-sleeping-cartoon-vector-icon-illustration-animal-nature-icon-concept-isolated-flat_138676-8171.jpg?t=st=1746434554~exp=1746438154~hmac=240b372ba7f23c72c96be3eb1f3f4af72016f60c6dc188d943627860045b2f4d&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-kangaroo-boxing-cartoon-vector-icon-illustration-animal-sport-icon-concept-isolated-vector_138676-4450.jpg?t=st=1746434996~exp=1746438596~hmac=cff2510a4ec829c30f7613c92c67e14abc32e4e0dc44f03e87002d66ee312642&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-fox-sitting-with-scarf-autumn-cartoon-icon-illustration-animal-nature-icon-isolated-flat-cartoon-style_138676-3115.jpg?t=st=1746434592~exp=1746438192~hmac=6fb1b8d6efe97727f76ef052dd5c987fe4f83f1211ffe404471f52fd59f392e6&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-lion-king-sitting-cartoon-vector-icon-illustration-animal-nature-icon-concept-isolated-premium-vector-flat-cartoon-style_138676-4256.jpg?t=st=1746434628~exp=1746438228~hmac=bd87a095e9e5b2c347d7c43a0e884ab69d4bac66d52ae1bb91df3972daed621a&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/hand-drawn-cartoon-evil-rat-illustration_52683-111836.jpg?t=st=1746434677~exp=1746438277~hmac=f76a21fbdcfd4b8d86c197f27d3ce4edc503e8afd3d46d3669c5918c2ecda03d&w=900"));
                pictureRepository.save(new ProfilePicture("https://img.freepik.com/free-vector/cute-polar-bears-surfing-cartoon-vector-icon-illustration-animal-sport-icon-concept-isolated-premium-vector-flat-cartoon-style_138676-4042.jpg?t=st=1746434732~exp=1746438332~hmac=f7480932ec598cb52aef9872ea6b8c0a8914e8275a77f28f68764a1394325a38&w=900"));
            }
        };
    }
}
