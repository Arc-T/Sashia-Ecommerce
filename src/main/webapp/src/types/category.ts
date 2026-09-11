import {zodResolver} from "@hookform/resolvers/zod";
import {useForm} from "react-hook-form";
import z from "zod";
import type {Pagination} from "~/types/page";

export interface Category {
    id: number;
    name: string;
    url: string;
    icon: string;
    parentId: number;
    displayOrder: number;
    description: string;
    createdAt: string;
    updatedAt: string;
    children?: Category[];
    parent?: Category;
}

export interface CategoryFilter extends Pagination {
    name: string;
    onlyParents: boolean;
    onlyChildren: boolean;
}

export type CategoryFormValues = z.infer<typeof CategorySchema>;

export const CategorySchema = z.object({
    name: z.string().min(1, "نام الزامی است"),
    url: z.string().min(1, "آدرس الزامی است"),
    icon: z.string().optional(),
    isParent: z.boolean(),
    displayOrder: z.coerce.number() as z.ZodNumber,
    description: z.string().optional(),
    parentId: z.number().optional(),
});

export const createCategoryForm = (category?: Category) =>
    useForm<CategoryFormValues>({
        resolver: zodResolver(CategorySchema),
        mode: "onSubmit",
        values: category
            ? {
                name: category.name,
                url: category.url,
                icon: category.icon,
                description: category.description,
                displayOrder: category.displayOrder,
                isParent: !category.parentId,
                parentId: category.parentId,
            }
            : undefined,
        defaultValues: {
            name: "",
            url: "",
            icon: "",
            description: "",
            displayOrder: 0,
            isParent: true,
            parentId: undefined,
        },
    });
