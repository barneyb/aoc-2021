import kotlin.jvm.internal.FunctionReferenceImpl
import aoc2015.day01_almost_lisp.main as day01_almost_lisp
import aoc2015.day02_wrap_presents.main as day02_wrap_presents
import aoc2015.day03_spherical_houses.main as day03_spherical_houses
import aoc2015.day04_adventcoin.main as day04_adventcoin
import aoc2018.day00_measure_up.main as day00_measure_up
import aoc2018.day01_chronal_calibration.main as day01_chronal_calibration
import aoc2018.day02_inventory_management.main as day02_inventory_management
import aoc2019.day01_rocket_equation.main as day01_rocket_equation
import aoc2019.day03_crossed_wires.main as day03_crossed_wires

/*INJECT:IMPORT*/

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
    /*INJECT:REF*/
)

fun main() {
    days.map {
        println((it as FunctionReferenceImpl).owner)
        it()
    }
}

