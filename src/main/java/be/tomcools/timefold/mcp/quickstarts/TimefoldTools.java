package be.tomcools.timefold.mcp.quickstarts;

import be.tomcools.timefold.quickstarts.shifts.api.DemoDataApi;
import be.tomcools.timefold.quickstarts.shifts.api.EmployeeSchedulesApi;
import be.tomcools.timefold.quickstarts.shifts.model.DemoData;
import be.tomcools.timefold.quickstarts.shifts.model.EmployeeSchedule;
import be.tomcools.timefold.quickstarts.shifts.model.ScoreAnalysis;
import be.tomcools.timefold.quickstarts.shifts.model.ScoreAnalysisFetchPolicy;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TimefoldTools {

    @RestClient
    DemoDataApi demoDataApi;

    @RestClient
    EmployeeSchedulesApi employeeSchedule;

    /**
     * This method transform the "SMALL" demo dataset using other skills and locations.
     * These have been customized.
     */
    @Tool(description = "Prepares the Timefold Demo Data with alternative industry details. This allows you to prepare demo data for a different industry than the regular nurse example. Relevant locations and skills for the alternative industry must be passed in. Before calling this tool, the user should be prompted for which industry it wants to prepare demo data.")
    ToolResponse setupIndustryDemoData(
            @ToolArg(description = "List of 3 examples of locations relevant for a industry. For the nurse set, these are Ambulatory care, Critical care, Pediatric care") List<String> locations,
            @ToolArg(description = "List of 4 examples of skills relevant for a industry. For the nurse set, these are Doctor, Nurse, Cardiology and Anaesthetics.") List<String> skills) {
        EmployeeSchedule demoData = demoDataApi.demoDataDemoDataIdGet(DemoData.SMALL);
        demoData.getEmployees().forEach(e -> {
            e.setSkills(e.getSkills().stream().map(s -> this.mapSkills(s,skills)).collect(Collectors.toSet()));
        });
        demoData.getShifts().forEach(s -> s.setRequiredSkill(mapSkills(s.getRequiredSkill(), skills)));
        demoData.getShifts().forEach(s -> s.setLocation(mapLocation(s.getLocation(), locations)));

        var id = employeeSchedule.schedulesPost(demoData);
        employeeSchedule.schedulesJobIdDelete(id); // immediately stop :)
        return ToolResponse.success(new TextContent("Optimization run started. Id is: " + id));
    }

    @Tool(description = "Analyzes the score for a schedule generated with a given ID")
    ToolResponse scoreAnalysis(
            @ToolArg(description = "The id (identifier) of a previously submitted schedule") String id) {
        EmployeeSchedule schedule = employeeSchedule.schedulesJobIdGet(id);
        ScoreAnalysis scoreAnalysis = employeeSchedule.schedulesAnalyzePut(schedule, ScoreAnalysisFetchPolicy.FETCH_ALL);
        return ToolResponse.success(new TextContent(scoreAnalysis.toString()));
    }

    private String mapLocation(String location, List<String> locations) {
        return location.replace("Ambulatory care", locations.get(0))
                .replace("Critical care", locations.get(1))
                .replace("Pediatric care", locations.get(2));
    }

    private String mapSkills(String skill, List<String> skills) {
        return skill.replace("Doctor", skills.get(0))
                .replace("Nurse", skills.get(1))
                .replace("Cardiology", skills.get(2))
                .replace("Anaesthetics", skills.get(3));
    }
}
