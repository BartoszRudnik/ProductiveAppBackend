package Xeva.productiveApp.localization;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.localization.dto.AddLocalization;
import Xeva.productiveApp.localization.dto.GetCoordinates;
import Xeva.productiveApp.task.Task;
import Xeva.productiveApp.task.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LocalizationService {

    private final AppUserService appUserService;
    private final LocalizationRepository localizationRepository;
    private final TaskRepository taskRepository;

    public GetCoordinates getCoordinates(Long id){

        GetCoordinates coordinates = new GetCoordinates();

        if(this.localizationRepository.findById(id).isPresent()) {

            Localization localization = this.localizationRepository.findById(id).get();

            coordinates.setLatitude(localization.getLatitude());
            coordinates.setLongitude(localization.getLongitude());

            return coordinates;
        }else{
            return null;
        }

    }

    public Localization getLocalization(Long id){

        if(this.localizationRepository.findById(id).isPresent()) {

            return this.localizationRepository.findById(id).get();

        }else{

            return null;

        }

    }

    public List<Localization> getLocalizations(String mail){

        boolean isValidUser = appUserService.findByEmail(mail).isPresent();

        if(!isValidUser){
            throw new IllegalStateException("Wrong user");
        }

        ApplicationUser user = appUserService.findByEmail(mail).get();

        return this.localizationRepository.findAllByUser(user);

    }

    public void save(Localization localization){
        this.localizationRepository.save(localization);
    }

    public Long addLocalization(String mail, AddLocalization addLocalization){

        boolean isValidUser = appUserService.findByEmail(mail).isPresent();

        if(!isValidUser){
            throw new IllegalStateException("Wrong user");
        }

        ApplicationUser user = appUserService.findByEmail(mail).get();

        Localization localization = new Localization(addLocalization.getLocalizationName(), addLocalization.getStreet(), addLocalization.getLocality(), addLocalization.getCountry(), addLocalization.getLongitude(), addLocalization.getLatitude(), user);

        this.localizationRepository.save(localization);

        return localization.getId();

    }

    public void deleteLocalization(Long id){

        if(this.localizationRepository.findById(id).isPresent()) {
            Localization localization = this.localizationRepository.findById(id).get();

            if(this.taskRepository.findAllByNotificationLocalization(localization).isPresent()) {

                List<Task> tasks = this.taskRepository.findAllByNotificationLocalization(localization).get();

                for(Task task : tasks){
                    task.setNotificationLocalization(null);
                    task.setNotificationOnExit(false);
                    task.setNotificationOnEnter(false);
                    task.setLocalizationRadius(0);

                    this.taskRepository.save(task);
                }

            }

            this.localizationRepository.delete(localization);

        }else{
            throw new IllegalStateException("Given localization doesn't exist");
        }

    }

    public void updateLocalization(Long id, AddLocalization addLocalization){

        boolean isValidLocalization = this.localizationRepository.findById(id).isPresent();

        if(!isValidLocalization){
            throw new IllegalStateException("Localization doesn't exists");
        }

        Localization localizationToEdit = this.localizationRepository.findById(id).get();

        localizationToEdit.setLocalizationName(addLocalization.getLocalizationName());
        localizationToEdit.setLatitude(addLocalization.getLatitude());
        localizationToEdit.setLongitude(addLocalization.getLongitude());
        localizationToEdit.setLocality(addLocalization.getLocality());
        localizationToEdit.setCountry(addLocalization.getCountry());
        localizationToEdit.setStreet(addLocalization.getStreet());

        this.localizationRepository.save(localizationToEdit);

    }

    public Optional<Localization> findById(Long id){
        if(id == null){
            return Optional.empty();
        }
        return this.localizationRepository.findById(id);
    }

    public void deleteAllUserLocalizations(ApplicationUser user){

        this.localizationRepository.deleteAllByUser(user);

    }

    public boolean localizationAlreadyExist(String mail, String localizationName) {
        ApplicationUser applicationUser = new ApplicationUser();

        if(this.appUserService.findByEmail(mail).isPresent()){
            applicationUser = this.appUserService.findByEmail(mail).get();
        }

        return this.localizationRepository.findByLocalizationNameAndUser(localizationName, applicationUser).isPresent();
    }
}
