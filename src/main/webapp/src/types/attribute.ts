import { useForm } from "react-hook-form";
import type { Category } from "./category";
import type { PaginatedRequest } from "./page";
import z from "zod";
import { zodResolver } from "@hookform/resolvers/zod";

export interface AttributeValueDTO {
  value: string;
}

export interface CreateAttributeDTO {
  name: string;
  type: string;
  isFilterable: boolean;
  values?: AttributeValueDTO[];
  categoryId: number;
  description?: string;
}

export interface Attribute {
  id: number;
  name: string;
  type: string;
  isFilterable: boolean;
  categoryId: number;
  description: string;
  createdAt: string;
  updatedAt: string;
  // ******************** Relations ********************.
  category: Category;
  values: AttributeValue[];
}

export interface AttributeValue {
  id: number;
  value: string;
}

export interface AttributeFilter extends PaginatedRequest {
  name: string;
}

// ********************************* Register *********************************

export const attributeFormSchema = z.object({
  name: z.string().min(2, { message: "نام ویژگی باید حداقل ۲ کاراکتر باشد" }),
  isFilterable: z.boolean(),
  categoryId: z.number({ error: "انتخاب دسته‌بندی الزامی است" }).positive(),
  type: z.string(),
  description: z.string().optional(),
  values: z.array(
    z.object({
      value: z.string().min(1),
    }),
  ),
});

export type CreateAttributeFormValues = z.infer<typeof attributeFormSchema>;

export const useCreateAttributeForm = (attribute?: Attribute) =>
  useForm<CreateAttributeFormValues>({
    resolver: zodResolver(attributeFormSchema),
    mode: "onSubmit",
    values: attribute && {
      name: attribute.name,
      categoryId: attribute.categoryId,
      isFilterable: attribute.isFilterable,
      type: attribute.type,
      description: attribute.description,
      values: attribute.values,
    },
    defaultValues: {
      name: "",
      categoryId: undefined,
      isFilterable: false,
      type: undefined,
      description: "",
      values: [],
    },
  });

// ****************** Search ******************

export const attributeSearchFormSchema = z.object({
  name: z.string().min(2, { message: "نام ویژگی باید حداقل ۲ کاراکتر باشد" }),
  // isFilterable: z.boolean(),
  // categoryId: z.number({error: "انتخاب دسته‌بندی الزامی است"}).positive(),
});

export type AttributeSearchFormValues = z.infer<
  typeof attributeSearchFormSchema
>;

export const useSearchAttributeForm = () =>
  useForm<AttributeSearchFormValues>({
    resolver: zodResolver(attributeSearchFormSchema),
    mode: "onSubmit",
    defaultValues: {
      name: "",
    },
  });
