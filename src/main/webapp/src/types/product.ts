import z from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import type { Category } from "~/types/category";
import type { PaginatedRequest } from "./page";
import type { ServiceGroup } from "./service-groups";

export interface Product {
  id: number;
  name: string;
  categoryId: number;
  shopId: number;
  quantity: number;
  price: number;
  serviceGroupId: number;
  discountId: number;
  discountAmount: number;
  status: string;
  description: string;
  createdAt: string;
  updatedAt: string;

  // ------------------------ Relations ------------------------
  category?: Category;
  serviceGroup?: ServiceGroup;
  media: ProductMedia[];
}

export interface ProductFilter extends PaginatedRequest { }

export interface ProductMedia {
  id: number;
  productId: number;
  mediaTypeId: number;
  url: string;
  displayOrder: number;
  extension: string;
  createdAt: string;
}

export const ProductSchema = z.object({
  name: z.string().min(1, "نام محصول الزامی است"),
  categoryId: z.number(),
  quantity: z.number().min(0, "تعداد نمی‌تواند منفی باشد"),
  price: z.number().min(0, "قیمت باید بزرگتر از صفر باشد"),
  description: z.string().optional(),
  serviceGroupId: z.number(),
  media: z.any().optional(),
});

export type ProductFormValues = z.infer<typeof ProductSchema>;

export const useProductForm = (product?: Product) =>
  useForm<ProductFormValues>({
    resolver: zodResolver(ProductSchema),
    mode: "onSubmit",
    values: product
      ? {
        name: product.name,
        categoryId: product.category?.id || 0,
        quantity: product.quantity,
        price: product.price,
        description: product.description,
        serviceGroupId: product.serviceGroup?.id || 0,
      }
      : undefined,
    defaultValues: {
      name: "",
      categoryId: undefined,
      quantity: 0,
      price: 0,
      description: "",
      serviceGroupId: undefined,
    },
  });
