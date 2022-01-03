package danu.ga.maps.algoritm

import unpas.ac.maps.algoritm.City
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.util.*


class FileReader {
    private var numCities = 0
    private var mutationRate = 0.0
    private var cities: Array<City?>
    private var curLine = 0
    private var st: StringTokenizer? = null
    private var arrayCount = 0
    private var x = 0
    private var y:Int = 0
    private var crossoverRate = 0.0
    private var fileName: String? = null

    constructor(fileName: String?) {
        numCities = 0
        mutationRate = 0.0
        cities = arrayOfNulls(0)
        curLine = 1
        arrayCount = 0
        this.fileName = fileName
        read()
    }

    fun getNumCities(): Int {
        return numCities
    }

    fun setNumCities(numCities: Int) {
        this.numCities = numCities
    }

    fun getMutationRate(): Double {
        return mutationRate
    }

    fun setMutationRate(mutationRate: Double) {
        this.mutationRate = mutationRate
    }

    fun getCities(): Array<City?>? {
        return cities
    }

    fun setCities(cities: Array<City?>) {
        this.cities = cities
    }

    fun getCurLine(): Int {
        return curLine
    }

    fun setCurLine(curLine: Int) {
        this.curLine = curLine
    }

    fun getSt(): StringTokenizer? {
        return st
    }

    fun setSt(st: StringTokenizer?) {
        this.st = st
    }

    fun getArrayCount(): Int {
        return arrayCount
    }

    fun setArrayCount(arrayCount: Int) {
        this.arrayCount = arrayCount
    }

    fun getX(): Int {
        return x
    }

    fun setX(x: Int) {
        this.x = x
    }

    fun getY(): Int {
        return y
    }

    fun setY(y: Int) {
        this.y = y
    }
    fun getCrossoverRate(): Double {
        return crossoverRate
    }

    fun setCrossoverRate(crossoverRate: Double) {
        this.crossoverRate = crossoverRate
    }

    fun read() {
        try {
            val `in` = BufferedReader(FileReader("./$fileName"))
            var line: String?
            try {
                while (`in`.readLine().also { line = it } != null) {
                    if (curLine == 1) {
                        st = StringTokenizer(line, "=")
                        st!!.nextToken()
                        numCities = st!!.nextToken().toInt()
                        cities = arrayOfNulls(numCities)
                    } else if (curLine == 2) {
                        st = StringTokenizer(line, "=")
                        st!!.nextToken()
                        mutationRate = st!!.nextToken().toDouble()
                    } else if (curLine == 3) {
                        st = StringTokenizer(line, "=")
                        st!!.nextToken()
                        crossoverRate = st!!.nextToken().toDouble()
                    } else {
                        st = StringTokenizer(line, "|")
                        st!!.nextToken()
                        val a = st!!.nextToken()
                        var stmp = StringTokenizer(a, "=")
                        stmp.nextToken()
                        x = stmp.nextToken().toInt()
                        val l = st!!.nextToken()
                        stmp = StringTokenizer(l, "=")
                        stmp.nextToken()
                        y = stmp.nextToken().toInt()
                        cities[arrayCount] = City(arrayCount, x.toDouble(), y.toDouble(),9,9)
                        arrayCount++
                    }
                    curLine++
                }
            } catch (e: Exception) {
            }
        } catch (e: FileNotFoundException) {
            println("File not found!!")
        }
    }

}