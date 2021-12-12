import day06_lanternfish_growth.main as day06_lanternfish_growth
import day07_treachery_of_whales.main as day07_treachery_of_whales
import day08_seven_segment.main as day08_seven_segment
import day09_smoke_basin.main as day09_smoke_basin
import day10_syntax_scoring.main as day10_syntax_scoring
import day11_dumbo_octopus.main as day11_dumbo_octopus
/*INJECT:IMPORT*/
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.terminal.Terminal
import util.Stopwatch
import kotlin.jvm.internal.CallableReference
import aoc2015.day01_almost_lisp.main as day01_almost_lisp
import aoc2015.day02_wrap_presents.main as day02_wrap_presents
import aoc2015.day03_spherical_houses.main as day03_spherical_houses
import aoc2015.day04_adventcoin.main as day04_adventcoin
import aoc2015.day05_intern_elves.main as day05_intern_elves
import aoc2015.day06_fire_hazard.main as day06_fire_hazard
import aoc2016.day01_no_time_for_taxi.main as day01_no_time_for_taxi
import aoc2016.day02_bathroom_security.main as day02_bathroom_security
import aoc2017.day01_inverse_captcha.main as day01_inverse_captcha
import aoc2017.day02_corruption_checksum.main as day02_corruption_checksum
import aoc2018.day00_measure_up.main as day00_measure_up
import aoc2018.day01_chronal_calibration.main as day01_chronal_calibration
import aoc2018.day02_inventory_management.main as day02_inventory_management
import aoc2019.day01_rocket_equation.main as day01_rocket_equation
import aoc2019.day02_program_alarm.main as day02_program_alarm
import aoc2019.day03_crossed_wires.main as day03_crossed_wires
import aoc2019.day04_secure_container.main as day04_secure_container
import aoc2020.day01_report_repair.main as day01_report_repair
import aoc2020.day02_password_philosophy.main as day02_password_philosophy
import aoc2020.day03_toboggan_trajectory.main as day03_toboggan_trajectory
import aoc2020.day04_passport_processing.main as day04_passport_processing
import aoc2020.day05_binary_boarding.main as day05_binary_boarding
import day01_sonar_sweep.main as day01_sonar_sweep
import day02_dive.main as day02_dive
import day03_binary_diagnostic.main as day03_binary_diagnostic
import day04_squid_bingo.main as day04_squid_bingo
import day05_hydrothermal_venture.main as day05_hydrothermal_venture
import ocean_profile.main as ocean_profile

private val days = listOf(
    ::day00_measure_up,
    ::day01_almost_lisp,
    ::day02_wrap_presents,
    ::day01_rocket_equation,
    ::day03_crossed_wires,
    ::day03_spherical_houses,
    ::day04_adventcoin,
    ::day01_chronal_calibration,
    ::day02_inventory_management,
    ::day01_no_time_for_taxi,
    ::day02_bathroom_security,
    ::day01_inverse_captcha,
    ::day02_corruption_checksum,
    ::day01_report_repair,
    ::day02_password_philosophy,
    ::day05_intern_elves,
    ::day06_fire_hazard,
    ::day03_toboggan_trajectory,
    ::day04_passport_processing,
    ::day05_binary_boarding,
    ::day02_program_alarm,
    ::day04_secure_container,
    ::day01_sonar_sweep,
    ::day02_dive,
    ::day03_binary_diagnostic,
    ::day04_squid_bingo,
    ::day05_hydrothermal_venture,
    ::day06_lanternfish_growth,
    ::day07_treachery_of_whales,
    ::day08_seven_segment,
    ::day09_smoke_basin,
    ::day10_syntax_scoring,
    ::day11_dumbo_octopus,
    /*INJECT:REF*/
    ::ocean_profile,
)

fun main() {
    val watch = Stopwatch()
    days.map {
        println((it as CallableReference).owner)
        it()
    }
    val elapsed = watch.elapsed
    Terminal().println("Total: " + TextStyles.bold("$elapsed"))
}

