const fs = require('fs');
const path = require('path');
const { exec } = require('child_process');

async function diagnoseAngularBuild() {
    console.log('Starting Angular build diagnostics...\n');

    const checks = [
        checkNodeVersion,
        checkMemoryUsage,
        checkPackageJson,
        checkAngularJson,
        checkNodeModules,
        checkTsConfig
    ];

    for (const check of checks) {
        try {
            await check();
            console.log('-------------------\n');
        } catch (error) {
            console.error(`Error during ${check.name}:`, error);
        }
    }
}

async function checkNodeVersion() {
    return new Promise((resolve) => {
        exec('node -v', (error, stdout) => {
            console.log('Node.js Version Check:');
            if (error) {
                console.log('❌ Unable to determine Node.js version');
            } else {
                const version = stdout.trim();
                const versionNum = parseInt(version.slice(1).split('.')[0]);
                if (versionNum < 14) {
                    console.log(`❌ Node.js version ${version} may be too old for Angular`);
                    console.log('ℹ️ Recommend using Node.js 14.x or higher');
                } else {
                    console.log(`✅ Node.js version ${version} is compatible`);
                }
            }
            resolve();
        });
    });
}

async function checkMemoryUsage() {
    const used = process.memoryUsage();
    console.log('Memory Usage Check:');
    console.log(`🔍 Memory Usage: ${Math.round(used.heapUsed / 1024 / 1024)}MB`);
    
    if (used.heapUsed > 1.5 * 1024 * 1024 * 1024) { // 1.5GB
        console.log('⚠️ High memory usage detected');
        console.log('ℹ️ Try increasing Node.js memory limit: NODE_OPTIONS=--max_old_space_size=4096');
    } else {
        console.log('✅ Memory usage is within normal range');
    }
}

async function checkPackageJson() {
    console.log('Package.json Check:');
    if (!fs.existsSync('package.json')) {
        console.log('❌ package.json not found');
        return;
    }

    const package = require(path.resolve('package.json'));
    console.log('Dependencies Check:');
    
    const criticalDeps = ['@angular/core', '@angular/cli', 'typescript'];
    criticalDeps.forEach(dep => {
        if (!package.dependencies[dep] && !package.devDependencies[dep]) {
            console.log(`❌ Missing critical dependency: ${dep}`);
        } else {
            console.log(`✅ Found ${dep}`);
        }
    });
}

async function checkAngularJson() {
    console.log('Angular.json Check:');
    if (!fs.existsSync('angular.json')) {
        console.log('❌ angular.json not found');
        return;
    }

    const angularConfig = require(path.resolve('angular.json'));
    if (!angularConfig.projects) {
        console.log('❌ No projects defined in angular.json');
        return;
    }
    console.log('✅ angular.json configuration found');
}

async function checkNodeModules() {
    console.log('node_modules Check:');
    if (!fs.existsSync('node_modules')) {
        console.log('❌ node_modules directory not found');
        console.log('ℹ️ Try running: npm install');
        return;
    }
    console.log('✅ node_modules directory exists');
}

async function checkTsConfig() {
    console.log('tsconfig.json Check:');
    if (!fs.existsSync('tsconfig.json')) {
        console.log('❌ tsconfig.json not found');
        return;
    }

    try {
        const tsConfig = require(path.resolve('tsconfig.json'));
        if (!tsConfig.compilerOptions) {
            console.log('⚠️ No compiler options found in tsconfig.json');
        } else {
            console.log('✅ tsconfig.json appears valid');
        }
    } catch (error) {
        console.log('❌ Invalid tsconfig.json format');
    }
}

diagnoseAngularBuild().catch(console.error);