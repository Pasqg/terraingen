package org.pasqg.terraingen.parameters;

public class ErosionParameters {
    private final long mSeed;

    private int mParticlesNum = 10000;
    private int mMapSizeX = 1024;
    private int mMapSizeY = 1024;

    private float mGravityAcceleration = 9.81f;

    private float mEvaporationRate = 0.01f;
    private float mDepositionRate = 0.1f;
    private int mDepositionRadius = 2;
    private float mErosionRate = 0.02f;
    /**
     * Minimum sediment to pick up, so that even if height = 0 (flat area) we are able to erode
     */
    private float mMinSedimentPickup = 0.1f;

    private float mParticleCapacity = 8.0f;
    private float mMinTerminalVelocity = 1.0f;
    private float mMaxTerminalVelocity = 10.0f;
    private float mMinParticleMass = 0.01f;
    private float mMaxParticleMass = 1000.0f;
    private float mParticleInertia = 0.001f;

    public ErosionParameters(long aSeed) {
        mSeed = aSeed;
    }

    public long getSeed() {
        return mSeed;
    }

    public int getParticlesNum() {
        return mParticlesNum;
    }

    public ErosionParameters setParticlesNum(int aParticlesNum) {
        mParticlesNum = aParticlesNum;
        return this;
    }

    public int getMapSizeX() {
        return mMapSizeX;
    }

    public ErosionParameters setMapSizeX(int aMapSizeX) {
        mMapSizeX = aMapSizeX;
        return this;
    }

    public int getMapSizeY() {
        return mMapSizeY;
    }

    public ErosionParameters setMapSizeY(int aMapSizeY) {
        mMapSizeY = aMapSizeY;
        return this;
    }

    public float getGravityAcceleration() {
        return mGravityAcceleration;
    }

    public ErosionParameters setGravityAcceleration(float aGravityAcceleration) {
        mGravityAcceleration = aGravityAcceleration;
        return this;
    }

    public float getEvaporationRate() {
        return mEvaporationRate;
    }

    public ErosionParameters setEvaporationRate(float aEvaporationRate) {
        mEvaporationRate = aEvaporationRate;
        return this;
    }

    public float getDepositionRate() {
        return mDepositionRate;
    }

    public ErosionParameters setDepositionRate(float aDepositionRate) {
        mDepositionRate = aDepositionRate;
        return this;
    }

    public int getDepositionRadius() {
        return mDepositionRadius;
    }

    public ErosionParameters setDepositionRadius(int aDepositionRadius) {
        mDepositionRadius = aDepositionRadius;
        return this;
    }

    public float getErosionRate() {
        return mErosionRate;
    }

    public ErosionParameters setErosionRate(float aErosionRate) {
        mErosionRate = aErosionRate;
        return this;
    }

    public float getMinSedimentPickup() {
        return mMinSedimentPickup;
    }

    public ErosionParameters setMinSedimentPickup(float aMinSedimentPickup) {
        mMinSedimentPickup = aMinSedimentPickup;
        return this;
    }

    public float getParticleCapacity() {
        return mParticleCapacity;
    }

    public ErosionParameters setParticleCapacity(float aParticleCapacity) {
        mParticleCapacity = aParticleCapacity;
        return this;
    }

    public float getMinTerminalVelocity() {
        return mMinTerminalVelocity;
    }

    public ErosionParameters setMinTerminalVelocity(float aMinTerminalVelocity) {
        mMinTerminalVelocity = aMinTerminalVelocity;
        return this;
    }

    public float getMaxTerminalVelocity() {
        return mMaxTerminalVelocity;
    }

    public ErosionParameters setMaxTerminalVelocity(float aMaxTerminalVelocity) {
        mMaxTerminalVelocity = aMaxTerminalVelocity;
        return this;
    }

    public float getMinParticleMass() {
        return mMinParticleMass;
    }

    public ErosionParameters setMinParticleMass(float aMinParticleMass) {
        mMinParticleMass = aMinParticleMass;
        return this;
    }

    public float getMaxParticleMass() {
        return mMaxParticleMass;
    }

    public ErosionParameters setMaxParticleMass(float aMaxParticleMass) {
        mMaxParticleMass = aMaxParticleMass;
        return this;
    }

    public float getParticleInertia() {
        return mParticleInertia;
    }

    public ErosionParameters setParticleInertia(float aParticleInertia) {
        mParticleInertia = aParticleInertia;
        return this;
    }
}
