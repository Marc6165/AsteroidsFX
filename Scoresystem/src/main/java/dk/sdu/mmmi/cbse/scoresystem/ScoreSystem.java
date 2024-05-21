package dk.sdu.mmmi.cbse.scoresystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class ScoreSystem {
    private long score = 0;

    public static void main(String[] args) {
        SpringApplication.run(ScoreSystem.class, args);
    }

    @GetMapping("/score")
    public long getScore() {
        return score;
    }

    @PutMapping ("/score/incrementScore")
    public void incrementScore() {
        score++;
    }

    @PutMapping("/score/add/{value}")
    public void addScoreBy(@PathVariable long value) {
        if(value > 0) {
            score += value;
        }
    }




}
