import { createBrowserRouter } from "react-router";
import { LoginPage } from "./routes/app/login";
import { RootLayout } from "./routes/app/root";
import { DashboardLayout } from "./routes/app/dashboard/route";
import { DashboardIndex } from "./routes/app/dashboard";
import { AttributesList } from "./routes/app/dashboard/attributes/list";
import { AttributesCreate } from "./routes/app/dashboard/attributes/create";
import { AttributesShow } from "./routes/app/dashboard/attributes/show";
import { CategoriesCreate } from "./routes/app/dashboard/categories/create";
import { CategoriesList } from "./routes/app/dashboard/categories/list";
import { CategoriesShow } from "./routes/app/dashboard/categories/show";
import { TagsCreate } from "./routes/app/dashboard/tags/create";
import { TagsList } from "./routes/app/dashboard/tags/list";
import { TagsShow } from "./routes/app/dashboard/tags/show";
import { ServicesCreate } from "./routes/app/dashboard/services/create";
import { ServicesList } from "./routes/app/dashboard/services/list";
import { ServicesShow } from "./routes/app/dashboard/services/show";
import { ProductsCreate } from "./routes/app/dashboard/products/create";
import { ProductsList } from "./routes/app/dashboard/products/list";
import { ProductsShow } from "./routes/app/dashboard/products/show";
import { AttributesEdit } from "./routes/app/dashboard/attributes/edit";

export const router = createBrowserRouter([
  {
    path: "dashboard",
    Component: RootLayout,
    children: [
      {
        Component: DashboardLayout,
        children: [
          {
            index: true,
            Component: DashboardIndex,
          },
          {
            path: "attributes",
            children: [
              {
                path: "create",
                Component: AttributesCreate,
              },
              {
                path: "list",
                Component: AttributesList,
              },
              {
                path: ":id?/show",
                Component: AttributesShow,
              },
              {
                path: ":id?/edit",
                Component: AttributesEdit,
              },
            ],
          },
          {
            path: "categories",
            children: [
              {
                path: "create",
                Component: CategoriesCreate,
              },
              {
                path: "list",
                Component: CategoriesList,
              },
              {
                path: ":id?/show",
                Component: CategoriesShow,
              },
            ],
          },
          {
            path: "tags",
            children: [
              {
                path: "create",
                Component: TagsCreate,
              },
              {
                path: "list",
                Component: TagsList,
              },
              {
                path: ":id?/show",
                Component: TagsShow,
              },
            ],
          },
          {
            path: "services",
            children: [
              {
                path: "create",
                Component: ServicesCreate,
              },
              {
                path: "list",
                Component: ServicesList,
              },
              {
                path: ":id?/show",
                Component: ServicesShow,
              },
            ],
          },
          {
            path: "products",
            children: [
              {
                path: "create",
                Component: ProductsCreate,
              },
              {
                path: "list",
                Component: ProductsList,
              },
              {
                path: ":id?/show",
                Component: ProductsShow,
              },
            ],
          },
        ],
      },
    ],
  },
  {
    path: "/login",
    Component: LoginPage,
  },
]);
