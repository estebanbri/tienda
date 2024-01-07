import { CartDTO } from "./cart-dto";

export interface PaymentIntentDTO {
    id?: number;
    description: string;
    cartDTO: CartDTO;
    currency: string;
    customer: string | null;
}