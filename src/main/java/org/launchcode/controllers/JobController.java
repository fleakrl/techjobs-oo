package org.launchcode.controllers;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model, int id) {

        // TODO #1 - get the Job with the given ID and pass it into the view
        JobData returnjob = JobData.getInstance();
        model.addAttribute("job", returnjob.findById(id));
        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }


    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) throws NullPointerException {

        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.
        Job createdJob = new Job();
        createdJob.setName(jobForm.getName());

        if (createdJob.getName().equals("")){
            String error = "name field is required";
            model.addAttribute(error);
            return "new-job";
        }

        Optional<CoreCompetency> competencyOptional = jobForm.getCoreCompetencies().stream()
                .filter(competency -> competency.getValue().equals(jobForm.getCoreCompetency()))
                .findFirst();

        if (competencyOptional.isPresent()) {
            createdJob.setCoreCompetency(competencyOptional.get());
        }

        Optional<Employer> employerOptional = jobForm.getEmployers().stream()
                .filter(employer -> employer.getId()==jobForm.getEmployerId())
                .findFirst();
        if(employerOptional.isPresent()){
            createdJob.setEmployer(employerOptional.get());
        }

        Optional<Location> locationOptional = jobForm.getLocations().stream()
                .filter(location -> location.getValue().equals(jobForm.getLocation()))
                .findFirst();
        if(locationOptional.isPresent()){
            createdJob.setLocation(locationOptional.get());
        }


        Optional<PositionType> positionTypeOptional = jobForm.getPositionTypes().stream()
                .filter(positionType -> positionType.getValue().equals(jobForm.getPositionType()))
                .findFirst();
        if(positionTypeOptional.isPresent()){
            createdJob.setPositionType(positionTypeOptional.get());
        }

                jobData.add(createdJob);

        model.addAttribute("job", createdJob);

        return "job-detail";

    }


}
