export interface UserDTO {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    country: string;
    role: string;
    email: string;
    accountActivated: boolean;
}

export interface UpdateUserDTO {
    firstName: string;
    lastName: string;
    country: string;
    email: string;
}