import {
  BarChart2,
  Briefcase,
  FolderTree,
  GalleryVerticalEnd,
  LayoutDashboard,
  ListFilter,
  MessageSquare,
  Package,
  Percent,
  ShoppingCart,
  Tags,
  Users,
} from "lucide-react";

export const sidebarItems = {
  user: {
    name: "shadcn",
    email: "m@example.com",
    avatar: "/avatars/shadcn.jpg",
  },
  teams: [
    {
      name: "Sashia E-Commerce",
      logo: GalleryVerticalEnd,
      plan: "Admin",
    },
    // {
    //   name: "Acme Corp.",
    //   logo: AudioWaveform,
    //   plan: "Startup",
    // },
    // {
    //   name: "Evil Corp.",
    //   logo: Command,
    //   plan: "Free",
    // },
  ],
  navMain: [
    // General
    {
      group: "general",
      title: "dashboard",
      url: "#",
      icon: LayoutDashboard,
      isActive: true,
    },
    {
      group: "general",
      title: "orders",
      url: "#",
      icon: ShoppingCart,
      items: [
        { title: "General", url: "#" },
        { title: "Team", url: "#" },
      ],
    },
    {
      group: "general",
      title: "customers",
      url: "#",
      icon: Users,
      items: [
        { title: "General", url: "#" },
        { title: "Team", url: "#" },
      ],
    },
    // Shop
    {
      group: "shop",
      title: "categories",
      url: "/categories",
      icon: FolderTree,
      items: [
        { title: "list", url: "/list" },
        { title: "create", url: "/create" },
        { title: "show", url: "/show" },
      ],
    },
    {
      group: "shop",
      title: "attributes",
      url: "/attributes",
      icon: ListFilter,
      items: [
        { title: "list", url: "/list" },
        { title: "create", url: "/create" },
        { title: "show", url: "/show" },
        { title: "edit", url: "/edit" },
      ],
    },
    {
      group: "shop",
      title: "tags",
      url: "/tags",
      icon: Tags,
      items: [
        { title: "list", url: "/list" },
        { title: "create", url: "/create" },
        { title: "show", url: "/show" },
      ],
    },
    {
      group: "shop",
      title: "products",
      url: "/products",
      icon: Package,
      items: [
        { title: "list", url: "/list" },
        { title: "create", url: "/create" },
        { title: "show", url: "/show" },
      ],
    },
    {
      group: "shop",
      title: "services",
      url: "/services",
      icon: Briefcase,
      items: [
        { title: "list", url: "/list" },
        { title: "create", url: "/create" },
        { title: "show", url: "/show" },
      ],
    },
    //Financial
    {
      group: "financial",
      title: "discounts",
      url: "#",
      icon: Percent,
      items: [
        { title: "General", url: "#" },
        { title: "Team", url: "#" },
      ],
    },
    //Report
    {
      group: "report",
      title: "analytics",
      url: "#",
      icon: BarChart2,
    },
    {
      group: "report",
      title: "reviews",
      url: "#",
      icon: MessageSquare,
    },
  ],
};
