export interface ActionRequestedByDTO {
    subject: string;
}

export interface ActionRequestedByWithTokenDTO extends ActionRequestedByDTO {
    token: string;
}