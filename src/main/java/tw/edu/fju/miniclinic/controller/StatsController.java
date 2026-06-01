package tw.edu.fju.miniclinic.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;

@Controller
public class StatsController {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @GetMapping("/stats")
    public String stats(Model model) {
        model.addAttribute("doctorCount", doctorRepo.count());
        model.addAttribute("patientCount", patientRepo.count());
        model.addAttribute("appointmentCount", appointmentRepo.count());
        
        List<Object[]> deptStats = appointmentRepo.countAppointmentsByDepartment();
        model.addAttribute("deptStats", deptStats);
        
        return "stats";
    }

    @GetMapping("/api/stats")
    @ResponseBody
    public Map<String, Object> apiStats() {
        Map<String, Long> byStatus = Map.of(
            "BOOKED", appointmentRepo.countByStatus("BOOKED"),
            "COMPLETED", appointmentRepo.countByStatus("COMPLETED"),
            "CANCELLED", appointmentRepo.countByStatus("CANCELLED")
        );

        return Map.of(
            "totalDoctors", doctorRepo.count(),
            "totalPatients", patientRepo.count(),
            "totalAppointments", appointmentRepo.count(),
            "byStatus", byStatus
        );
    }
}