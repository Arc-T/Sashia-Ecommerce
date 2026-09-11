import type { CategoryFilter } from "./category";
import type { ServiceFilter } from "./services";
import type { ServiceGroupFilter } from "./service-groups";
import type { ProductFilter } from "./product";
import type { OrderFilter } from "./order";
import type { AttributeFilter } from "./attribute";
import { toQueryString } from "@/lib/utils";

export const local = {
  media: {
    showDraft: (mediaName: string, productId?: number) =>
      productId
        ? `http://localhost:8080/media/draft/products/${productId}/${mediaName}`
        : `http://localhost:8080/media/draft/${mediaName}`,
    showProductMedia: (productId: number, mediaName: string) =>
      `http://localhost:8080/media/products/${productId}/${mediaName}`,
  },
};

export const api = {
  authentication: {
    validate: "/authentication/validate",
    logout: "/authentication/logout",
    login: "/authentication",
  },
  products: {
    create: "/products",
    get: (id: number) => `/products/${id}`,
    list: (filter?: Partial<ProductFilter>) =>
      `/products${toQueryString(filter)}`,
    details: (id: number) => `/products/${id}`,
    update: (id: number) => `/products/${id}`,
  },
  productMedia: {
    delete: (id: number) => `/product-media/${id}`,
    showProductMedia: (productId: number) =>
      `/product-media/products/${productId}`,
    draft: (productId?: number) =>
      productId ? `/product-media/draft/${productId}` : `/product-media/draft`,
    deleteDraft: (mediaName: string, productId?: number) =>
      productId
        ? `/product-media/draft/${productId}/${mediaName}`
        : `/product-media/draft/${mediaName}`,
  },
  attributes: {
    list: (filter?: Partial<AttributeFilter>) =>
      `/attributes${toQueryString(filter)}`,
    update: (id: number) => `/attributes/${id}`,
    details: (id: number) => `/attributes/${id}`,
    delete: (id: number) => `/attributes/${id}`,
    types: "/attributes/types",
    create: "/attributes",
  },
  categories: {
    list: (filter?: Partial<CategoryFilter>) =>
      `/categories${toQueryString(filter)}`,
    update: (id: number) => `/categories/${id}`,
    details: (id: number) => `/categories/${id}`,
    create: "/categories",
  },
  services: {
    list: (filter?: Partial<ServiceFilter>) =>
      `/services${toQueryString(filter)}`,
    update: (id: number) => `/services/${id}`,
    details: (id: number) => `/services/${id}`,
    create: "/services",
  },
  orders: {
    list: (filter?: Partial<OrderFilter>) => `/orders${toQueryString(filter)}`,
    details: (id: number) => `/orders/${id}`,
  },
  serviceGroups: {
    list: (filter?: Partial<ServiceGroupFilter>) =>
      `/service-groups${toQueryString(filter)}`,
    update: (id: number) => `/service-groups/${id}`,
    details: (id: number) => `/service-groups/${id}`,
    create: "/service-groups",
  },
  auth: {
    login: "/auth/login",
    logout: "/auth/logout",
  },
};
