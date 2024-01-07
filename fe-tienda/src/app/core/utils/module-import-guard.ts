export function throwIfAlreadyLoaded(parentModule: any, moduleName: string) {
    if(parentModule) {
       throw new Error(`${moduleName} ya fue cargado. Importa el modulo core dentro del modulo AppModule unicamente`);
    }
}