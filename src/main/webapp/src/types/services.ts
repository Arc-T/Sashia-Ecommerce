import {zodResolver} from "@hookform/resolvers/zod";
import {useForm} from "react-hook-form";
import z from "zod";
import type { PaginatedRequest } from "./page";

export interface Service {
    id: number;
    name: string;
    cost: number;
    serviceGroupId: number;
    description: string;
    createdAt: string;
    updatedAt: string;
}

export interface ServiceFilter extends PaginatedRequest {
    name: string;
}

export const ServiceSchema = z.object({
    name: z.string().min(1, "نام محصول الزامی است"),
    cost: z.number().min(0, "هزینه باید بزرگتر از صفر باشد"),
    serviceGroupId: z
        .number({error: "انتخاب گروه سرویس الزامی است"})
        .positive(),
    description: z.string().optional(),
});

export type ServiceFormValues = z.infer<typeof ServiceSchema>;

export const createServiceForm = (service?: Service) =>
    useForm<ServiceFormValues>({
        resolver: zodResolver(ServiceSchema),
        mode: "onSubmit",
        values: service
            ? {
                name: service.name,
                cost: service.cost,
                serviceGroupId: service.serviceGroupId,
                description: service.description,
            }
            : undefined,
        defaultValues: {
            name: "",
            cost: 0,
            serviceGroupId: undefined,
            description: "",
        },
    });
