import { ChevronRight, type LucideIcon } from "lucide-react";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "@/components/ui/collapsible";
import {
  SidebarGroup,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarMenuSub,
  SidebarMenuSubButton,
  SidebarMenuSubItem,
} from "@/components/ui/sidebar";
import { useTranslation } from "react-i18next";
import { Link } from "react-router";

export function NavMain({
  items,
}: {
  items: {
    title: string;
    url: string;
    icon?: LucideIcon;
    isActive?: boolean;
    group?: string;
    items?: {
      title: string;
      url: string;
    }[];
  }[];
}) {
  const groupedItems = items.reduce<Record<string, typeof items>>(
    (acc, item) => {
      const group = item.group || "general";
      if (!acc[group]) acc[group] = [];
      acc[group].push(item);
      return acc;
    },
    {},
  );
  const { t, i18n } = useTranslation();

  return (
    <>
      {Object.entries(groupedItems).map(([groupName, groupItems]) => (
        <SidebarGroup key={groupName}>
          <SidebarGroupLabel>{t(groupName)}</SidebarGroupLabel>
          <SidebarMenu>
            {groupItems.map((item) =>
              item.items && item.items.length > 0 ? (
                <Collapsible
                  key={item.title}
                  asChild
                  defaultOpen={item.isActive}
                  className="group/collapsible"
                >
                  <SidebarMenuItem>
                    <CollapsibleTrigger asChild>
                      <SidebarMenuButton tooltip={item.title}>
                        {item.icon && <item.icon />}
                        <span>{t(item.title)}</span>
                        <ChevronRight
                          className={`ml-auto transition-transform duration-200 group-data-[state=open]/collapsible:rotate-90 ${
                            i18n.dir() === "rtl" ? "rotate-180" : ""
                          }`}
                        />
                      </SidebarMenuButton>
                    </CollapsibleTrigger>
                    <CollapsibleContent>
                      <SidebarMenuSub>
                        {item.items.map((subItem) => (
                          <SidebarMenuSubItem key={subItem.title}>
                            <SidebarMenuSubButton asChild>
                              <Link
                                to={
                                  `/dashboard/${item.url}${subItem.url}` as string
                                }
                                // activeProps={{
                                //   className:
                                //     "bg-primary text-white dark:text-black",
                                // }}
                                // activeOptions={{
                                //   exact: true,
                                // }}
                              >
                                {t(subItem.title)}
                              </Link>
                            </SidebarMenuSubButton>
                          </SidebarMenuSubItem>
                        ))}
                      </SidebarMenuSub>
                    </CollapsibleContent>
                  </SidebarMenuItem>
                </Collapsible>
              ) : (
                <SidebarMenuItem key={item.title}>
                  <SidebarMenuButton tooltip={item.title} asChild>
                    <Link
                      to={`/dashboard/${item.url}` as string}
                      className="flex items-center gap-2"
                      // activeProps={{
                      //   className: "bg-primary text-white dark:text-black",
                      // }}
                      // activeOptions={{
                      //   exact: true,
                      // }}
                    >
                      {item.icon && <item.icon />}
                      <span>{t(item.title)}</span>
                    </Link>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ),
            )}
          </SidebarMenu>
        </SidebarGroup>
      ))}
    </>
  );
}
