const fs = require('fs');
const path = require('path');

// Path configurations
const artifactsDir = path.join(__dirname, '../artifacts/contracts/BookingContract.sol');
const targetResourcesDir = path.join(__dirname, '../../backend/queute/src/main/resources/contracts');

async function exportArtifacts() {
  console.log('Exporting contract artifacts to Java project...');
  
  try {
    // Read the contract artifact
    const contractJson = require(path.join(artifactsDir, 'BookingContract.json'));
    
    // Ensure target directory exists
    if (!fs.existsSync(targetResourcesDir)) {
      console.log(`Creating directory: ${targetResourcesDir}`);
      fs.mkdirSync(targetResourcesDir, { recursive: true });
    }
    
    // Extract ABI
    const abi = JSON.stringify(contractJson.abi, null, 2);
    fs.writeFileSync(path.join(targetResourcesDir, 'BookingContract.abi'), abi);
    console.log('ABI exported successfully.');
    
    // Extract bytecode
    fs.writeFileSync(path.join(targetResourcesDir, 'BookingContract.bin'), contractJson.bytecode);
    console.log('Bytecode exported successfully.');
    
    console.log('All artifacts exported successfully!');
    console.log(`Files saved to: ${targetResourcesDir}`);
  } catch (error) {
    console.error('Error exporting artifacts:', error.message);
  }
}

// Run the export function
exportArtifacts(); 