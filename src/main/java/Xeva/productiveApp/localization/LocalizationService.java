package Xeva.productiveApp.localization;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.localization.dto.AddLocalization;
import Xeva.productiveApp.localization.dto.GetCoordinates;
import Xeva.productiveApp.task.Task;
import Xeva.productiveApp.task.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LocalizationService {

    private final AppUserService appUserService;
    private final LocalizationRepository localizationRepository;
    private final TaskRepository taskRepository;

    public GetCoordinates getCoordinates(String uuid){

        GetCoordinates coordinates = new GetCoordinates();

        if(this.localizationRepository.findByUuid(uuid).isPresent()) {

            Localization localization = this.localizationRepository.findByUuid(uuid).get();

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

        Localization localization = new Localization(addLocalization.getLocalizationName(), addLocalization.getStreet(), addLocalization.getLocality(), addLocalization.getCountry(), addLocalization.getLongitude(), addLocalization.getLatitude(), user, addLocalization.getUuid());

        this.localizationRepository.save(localization);

        return localization.getId();

    }

    public void deleteLocalization(String uuid){

        if(this.localizationRepository.findByUuid(uuid).isPresent()) {
            Localization localization = this.localizationRepository.findByUuid(uuid).get();

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
            throw new IllegalStateException("Localization doesn't exist");
        }

    }

    public void updateLocalization(String uuid, AddLocalization addLocalization){

        boolean isValidLocalization = this.localizationRepository.findByUuid(uuid).isPresent();

        if(!isValidLocalization){
            throw new IllegalStateException("Localization doesn't exists");
        }

        Localization localizationToEdit = this.localizationRepository.findByUuid(uuid).get();

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

    public boolean localizationAlreadyExist(String uuid) {
        return this.localizationRepository.findByUuid(uuid).isPresent();
    }

    public Localization findByUuid(String uuid) {
        if(this.localizationRepository.findByUuid(uuid).isEmpty()){
            throw new IllegalStateException("Localization doesn't exist");
        }

        return this.localizationRepository.findByUuid(uuid).get();
    }
}
