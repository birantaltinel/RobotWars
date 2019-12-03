    double[] enemyCoordinates = scan();
    if(enemyCoordinates != null && isCannonReloaded()) {
        fire(enemyCoordinates[0], enemyCoordinates[1]);
    }
    else {
        move(getDirection() + 5, 1);
    }



