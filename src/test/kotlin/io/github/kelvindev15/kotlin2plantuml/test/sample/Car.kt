package io.github.kelvindev15.kotlin2plantuml.test.sample

interface Vehicle<T> {
    fun start()

    fun stop()
}

abstract class AbstractVehicle<T> : Vehicle<T> {
    abstract fun description(): String

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}

interface Wheeled<T, P> : Vehicle<T>

interface TwoWheels<T, P> : Wheeled<T, P>

interface FourWheels<T, P, A> : Wheeled<T, P>

class Bicycle<T, P> :
    AbstractVehicle<T>(),
    TwoWheels<T, P> {
    override fun description(): String {
        TODO("Not yet implemented")
    }
}

interface OutOfThree

interface ComplexGeneric<T, P, A> where T : FourWheels<T, P, A>, P : Wheeled<T, P>

class Car<T, O, A : ComplexGeneric<T, O, A>> :
    AbstractVehicle<T>(),
    OutOfThree,
    FourWheels<T, O, Vehicle<T>>
    where T : FourWheels<T, O, A>,
          O : Wheeled<T, O> {
    override fun description(): String {
        TODO("Not yet implemented")
    }

    @Suppress("UnusedPrivateMember")
    private val privateField = ""

    @Suppress("EmptyFunctionBlock", "UnusedPrivateMember")
    private fun privateMethod() {}

    @Suppress("UnusedPrivateMember")
    val bike: Bicycle<T, O>? = null
}

@Suppress("UnusedPrivateClass")
private class PrivateCar<T> :
    AbstractVehicle<T>(),
    OutOfThree {
    override fun description(): String {
        TODO("Not yet implemented")
    }
}
