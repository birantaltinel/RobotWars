    double[] enemyCoordinates = scan();
    if(enemyCoordinates != null && isCannonReloaded()) {
        fire(enemyCoordinates[0], enemyCoordinates[1]);
    }
    else if(isCannonReloaded()) {
        fire(Math.random() * 500, Math.random() * 500 );
    }



