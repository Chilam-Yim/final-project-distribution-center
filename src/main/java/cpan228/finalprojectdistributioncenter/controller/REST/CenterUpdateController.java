package cpan228.finalprojectdistributioncenter.controller.REST;

import java.util.List;
import java.util.Optional;

import cpan228.finalprojectdistributioncenter.model.DistributionCenter;
import cpan228.finalprojectdistributioncenter.model.Item;
import cpan228.finalprojectdistributioncenter.model.dto.CreateCenter;
import cpan228.finalprojectdistributioncenter.repository.DistributionCenterRepository;
import cpan228.finalprojectdistributioncenter.repository.DistributionCenterRepositoryPaginated;
import cpan228.finalprojectdistributioncenter.repository.ItemRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;


@RestController
@RequestMapping(path = "/api/update", produces = "application/json")
public class CenterUpdateController {

    private final DistributionCenterRepositoryPaginated centerRepositoryPaginated;
    private final DistributionCenterRepository centerRepository;

    private final ItemRepository itemRepository;

    public CenterUpdateController(DistributionCenterRepositoryPaginated centerRepositoryPaginated,
                                DistributionCenterRepository centerRepository, ItemRepository itemRepository) {
        this.centerRepositoryPaginated = centerRepositoryPaginated;
        this.centerRepository = centerRepository;
        this.itemRepository = itemRepository;
    }

    @PutMapping("/{id}")
    public DistributionCenter updateCenter(@PathVariable("id") Long id,
                                           @Valid @RequestBody CreateCenter center) {
        var centerToUpdate = centerRepository.findById(id).orElseThrow();
        centerToUpdate.setName(center.getName());
        centerToUpdate.setItems(center.getItems());
        centerToUpdate.setLatitude(center.getLatitude());
        centerToUpdate.setLongitude(center.getLongitude());
        return centerRepository.save(center.toDistributionCenter());
    }

}
